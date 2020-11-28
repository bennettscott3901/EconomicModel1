package marketSim;

import java.util.*;
/*import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;*/

public class MarketSimulation {
	public static Agent[] arrAgents;
	public static Market dow;
	public static Market previousDow;

	public static void main(String[] args) {
		// setup
		setup();
		// running of the program
		while (true) {
			int steps = 0;
			while (true) {
				try {
					Scanner input = new Scanner(System.in);
					System.out.print("Input number of steps to be taken: ");
					steps = input.nextInt();
					if (steps > 0)
						break;
					System.out.println("Invalid number of steps.");
				} catch (Exception e) {
					System.out.println("Invalid input.");
				}
			}
			for (int i = 0; i < steps; i++)
				step();
			print();
		}
	}

	// initializes agents, market
	public static void setup() {
		int agentNum = 0;
		while (true) {
			try {
				Scanner input = new Scanner(System.in);
				System.out.print("Number of Agents: ");
				agentNum = input.nextInt();
				if (agentNum > 0)
					break;
				System.out.println("Invalid number of agents.");
			} catch (Exception e) {
				System.out.println("Incorrect input type.");
			}
		}
		arrAgents = new Agent[agentNum];
		dow = new Market(agentNum);
		previousDow = dow;
		Object[] portions = divideMarket(arrAgents.length);
		for (int i = 0; i < arrAgents.length; i++)
			arrAgents[i] = new Agent((double) portions[i]);
		print();
	}

	// divides the market up between the agents, currently VERY unequal
	public static Object[] divideMarket(int l) {
		List<Double> ret = new ArrayList<>();
		double remaining = dow.getIndex();
		// assigns base amount of market to avoid absurd inequality
		// multiplier of l and divisor of remaining is how much of market
		// is being put towards baseline
		double baseline = remaining / (l * 2);
		remaining = remaining / 2;
		for (int i = 0; i < l; i++) {
			ret.add(baseline);
		}
		Random rand = new Random();
		// assigns random amount of market
		for (int i = 0; i < l - 1; i++) {
			double randomPortion = rand.nextDouble() * remaining;
			ret.set(i, ret.get(i) + randomPortion);
			remaining -= randomPortion;
		}
		ret.set(l - 1, ret.get(l - 1) + remaining);
		Collections.shuffle(ret);
		return ret.toArray();
	}

	// takes a 'step' (a day in the current simulation) in the financial market
	public static void step() {
		// makes a clone so agents are only working with previous step's information
		Agent[] sto = arrAgents.clone();
		//random confidence change for the day, last number may be changed to change volatility
		double randomGlobalConfidence = ((Math.random() * 2) - 1)/100;
		for (int i = 0; i < arrAgents.length; i++)
			arrAgents[i].agentStep(i, sto, dow, previousDow, randomGlobalConfidence);
		previousDow.setIndex(dow.getIndex());
		dow.marketStep(arrAgents);
	}

	// prints the current market and agent values
	public static void print() {
		// print agents
		for (int i = 0; i < arrAgents.length; i++) {
			System.out.println("Agent " + (i + 1) + ":");
			System.out.println(arrAgents[i].toString());
		}
		System.out.println("DOW: " + dow);
		// divides steps with a line for readability
		for (int i = 0; i < 70; i++)
			System.out.print("-");
		System.out.println();
	}
}
