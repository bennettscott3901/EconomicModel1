package marketSim;

import java.text.DecimalFormat;

public class Agent {
	private double confidence;
	private double currentInvestment;
	private double nonInvested;
	private double volatility;

	public Agent(double cI) {
		confidence = (Math.random() * 2) - 1;
		currentInvestment = cI;
		nonInvested = cI * (Math.random() * 2);
		// volatility of agent, higher number = higher volatility
		volatility = Math.random();
	}

	// step is one 'step' in the market, currently a day, passing
	// this is the interval in which investments will be made,
	// confidence will be changed, etc.
	public void agentStep(int i, Agent[] currentAgents, Market m, Market previous, double globalConfidence) {
		applyInterest(m, previous);
		changeConfidence(i, currentAgents, m, previous, globalConfidence);
		changeInvestment();
	}
	//applies changes to investment
	//PROBLEM: tends towards very large numbers
	public void applyInterest(Market m, Market previous) {
		// add to nonInvested a set growth rate per day
		nonInvested += nonInvested * .02/365;
		// increase value of investments based on how the market did between this and
		// previous step and relative investment of agent
		//currentInvestment += (m.getIndex() - previous.getIndex())*(currentInvestment/m.getIndex())/1000;
	}

	// changes relative confidence of agent
	// UPDATE to be based on relative gains
	private void changeConfidence(int index, Agent[] agents, Market m, Market previous, double globalConfidence) {
		// average confidence
		// weighted confidence for neighbors
		// Increases/decrease confidence based on current investment in the market
		if (m.getIndex() > previous.getIndex())
			confidence += ((currentInvestment) / 10000) * (m.getIndex() / previous.getIndex());
		else if (m.getIndex() < previous.getIndex())
			confidence -= ((currentInvestment) / 10000) * (previous.getIndex() / m.getIndex());
		// random confidence changes, individual and global
		confidence += globalConfidence;
		confidence += (Math.random() * 2 - 1) / 100;
		// Places limits on levels of confidence
		if (confidence > 1)
			confidence = 1;
		else if (confidence < -1)
			confidence = -1;
	}

	// changes the amount currently invested in the market by an agent
	// PROBLEM: tends to go to extremes: none in or none saved
	private void changeInvestment() {
		if (confidence > 0) {
			double puttingIn = nonInvested * confidence / 10;
			// Cannot remove more from the market than they have
			if (puttingIn + currentInvestment < 0)
				puttingIn = currentInvestment;
			currentInvestment += puttingIn;
			nonInvested -= puttingIn;
		} else if (confidence < 0) {
			double takingOut = currentInvestment * Math.abs(confidence) / 10;
			if (currentInvestment - takingOut < 0)
				takingOut = currentInvestment;
			currentInvestment -= takingOut;
			nonInvested += takingOut;
		}
	}

	public double getConfidence() {
		return confidence;
	}

	public double getCurrentInvestment() {
		return currentInvestment;
	}

	// toString, format to be uniform
	public String toString() {
		DecimalFormat investmentFormat = new DecimalFormat("#.00");
		DecimalFormat feelingFormat = new DecimalFormat("#.0000");
		String ret = new String("Confidence: " + feelingFormat.format(confidence));
		ret += ", Current Investment: " + investmentFormat.format(currentInvestment);
		ret += ", Non invested: " + investmentFormat.format(nonInvested);
		return ret;
	}
}
