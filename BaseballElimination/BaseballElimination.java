import java.util.Hashtable;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Queue;

public class BaseballElimination {
	private int numberOfTeams;
	private Hashtable<String, InfoOfTeam> teams;
	private Hashtable<Integer, Integer> v2id;
	private String[] id2TeamName;
	private int[][] g;
	private int V;
	private int flowFromS;
	private int matchesOfTeams;
	
	private class InfoOfTeam {
		int id;
		int wins;
		int loss;
		int left;		
		public InfoOfTeam(int id, int wins, int loss, int left) {
			this.id = id;
			this.wins = wins;
			this.loss = loss;
			this.left = left;
		}
	}
	
	/*
	 * create a baseball division from given filename in format specified below
	 */
	public BaseballElimination(String filename) {
		In in = new In(filename);
		this.numberOfTeams = Integer.parseInt(in.readLine());
		teams = new Hashtable<String, InfoOfTeam>();
		g = new int[numberOfTeams][numberOfTeams];
		id2TeamName = new String[numberOfTeams];	
		v2id = new Hashtable<Integer, Integer>();
		
		int id  = 0;
		while (in.hasNextLine()) {
			String line = in.readLine().trim();
			String[] tokens = line.split(" +");
			String key = tokens[0];
			int wins = Integer.parseInt(tokens[1]);
			int loss = Integer.parseInt(tokens[2]);
			int left = Integer.parseInt(tokens[3]);
			teams.put(key, new InfoOfTeam(id, wins, loss, left));
			id2TeamName[id] = key;
			
			for(int i = 0; i < this.numberOfTeams; i++) {
				g[id][i] = Integer.parseInt(tokens[4+i]);
			}
			id++;
		}
	}
	
	/*
	 * number of teams
	 */
	public int numberOfTeams() {
		return this.numberOfTeams;
	}
	
	/*
	 * all teams
	 */
	public Iterable<String> teams() {
		return this.teams.keySet();
	}
	
	/*
	 * number of wins for given team
	 */
	public int wins(String team) {
		isValid(team);
		return this.teams.get(team).wins;
	}
	
	/*
	 * number of losses for given team
	 */
	public int losses(String team) {
		isValid(team);
		return this.teams.get(team).loss;
	}
	
	/*
	 * number of remaining games for given team
	 */
	public int remaining(String team) {
		isValid(team);
		return this.teams.get(team).left;
	}
	
	/*
	 * number of remaining games between team1 and team2
	 */
	public int against(String team1, String team2) {
		isValid(team1);
		isValid(team2);
		int i = this.teams.get(team1).id;
		int j = this.teams.get(team2).id;
		return this.g[i][j];
	}
	
	/*
	 * is given team eliminated?
	 */
	public boolean isEliminated(String team) {
		isValid(team);
		FlowNetwork G = constructFlowNetwork(team);
		if(G == null) return true;
		FordFulkerson maxflow = new FordFulkerson(G, 0, V-1);
		return this.flowFromS > maxflow.value();
	}
	
	/*
	 * subset R of teams that eliminates given team; null if not eliminated
	 */
	public Iterable<String> certificateOfElimination(String team) {
		isValid(team);
		if(!isEliminated(team))
			return null;
		else {
			Queue<String> certificates = new Queue<String>(); 
			int idOfGivenTeam = this.teams.get(team).id;
			FlowNetwork G = constructFlowNetwork(team);
			if(G == null) {
				int mostWins = teams.get(team).wins + teams.get(team).left;
				for(int i = 0; i < this.numberOfTeams; i++) {
					if (i == idOfGivenTeam) continue;
					InfoOfTeam teamInfo = teams.get(this.id2TeamName[i]);
					if(mostWins < teamInfo.wins)
						certificates.enqueue(this.id2TeamName[i]);
				}
			}else {
				FordFulkerson maxflow = new FordFulkerson(G, 0, V-1);				
				for(int v = 1+this.matchesOfTeams; v < this.numberOfTeams+this.matchesOfTeams; v++) {
					if(maxflow.inCut(v)) {
						int id = this.v2id.get(v);
						certificates.enqueue(this.id2TeamName[id]);
					}
				}	
			}
			return certificates;
		}
	}
	
	private void isValid(String team) {
		if (!this.teams.containsKey(team))
			throw new  java.lang.IllegalArgumentException();
	}
	
	private FlowNetwork constructFlowNetwork(String team) {
		InfoOfTeam teamInfo = this.teams.get(team);
		this.matchesOfTeams = (this.numberOfTeams-1)*(this.numberOfTeams-2)/2;
		this.V = matchesOfTeams + this.numberOfTeams+1;
		FlowNetwork G = new FlowNetwork(V);
		int id = teamInfo.id; 
		int mostWins = teamInfo.wins + teamInfo.left;
		this.flowFromS = 0;
		int index = 1, s = 0, t = V-1;
		for(int i=0, m_i=i; i < this.numberOfTeams; i++,m_i++) {
			if(i == id) {
				m_i--;
				continue;	
			}
			for(int j=i+1, m_j=m_i+1; j < this.numberOfTeams; j++,m_j++) {
				if(j == id) {
					m_j--;
					continue;
				}
				G.addEdge(new FlowEdge(s, index, g[i][j]));
				this.flowFromS = this.flowFromS + g[i][j];
				G.addEdge(new FlowEdge(index, m_i+1+matchesOfTeams, Double.POSITIVE_INFINITY));
				G.addEdge(new FlowEdge(index, m_j+1+matchesOfTeams, Double.POSITIVE_INFINITY));
				v2id.put(m_i+1+matchesOfTeams, i);
				v2id.put(m_j+1+matchesOfTeams, j);
				index++;
			}
			int againstTeamWins = this.teams.get(this.id2TeamName[i]).wins;
			if((mostWins-againstTeamWins) < 0) return null;
			G.addEdge(new FlowEdge(m_i+1+matchesOfTeams, t, mostWins-againstTeamWins));
		}		
		return G;
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team))
					StdOut.print(t + " ");
				StdOut.println("}");
			}
			else
				StdOut.println(team + " is not eliminated");
		}
	}
}
