package marketSim;

public class Market {
	private double index;

	public Market(int agentNum) {
		// Meant to have comparable numbers to DOW Jones with 100 simulated individuals
		index = 100 * agentNum + (Math.random() * 50 * agentNum);
	}

	public void marketStep(Agent[] agents) {
		index = 0;
		for (int i = 0; i < agents.length; i++)
			index += agents[i].getCurrentInvestment();
	}

	public double getIndex() {
		return index;
	}

	public boolean setIndex(double i) {
		index = i;
		return true;
	}

	public String toString() {
		String indexStr = new String(Double.toString(index));
		return indexStr;
	}
}
