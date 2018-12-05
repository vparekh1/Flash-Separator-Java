package numericmethods;

//Class for solving a cubic equation. 
public class CubicSolve {

	public CubicSolve() {
		super();
	}

	public double[] solve(CubicEquation f) {
		double a = f.getA();
		double b = f.getB();
		double c = f.getC();

		double[] result = new double[2];

		// Get the discriminant of the cubic function
		double disc, Q, R;
		Q = (a*a - 3. * b) / 9.;
		R = (2. * a*a*a - 9. * a * b + 27. * c) / 54.;
		disc = R*R - Q*Q*Q;

		// If discriminant is less than zero, we have three roots
		// else, we have one real root
		if (disc <= 0) {
			// Three roots
			double theta = Math.acos(R * Math.pow(Q, -1.5));
			double x1, x2, x3;
			x1 = -2. * Math.sqrt(Q) * Math.cos(theta / 3.) - a / 3.;
			x2 = -2. * Math.sqrt(Q) * Math.cos((theta + 2. * Math.PI) / 3.) - a
					/ 3.;
			x3 = -2. * Math.sqrt(Q) * Math.cos((theta - 2. * Math.PI) / 3.) - a
					/ 3.;

			result[0] = Math.min(Math.min(x1, x2), x3);
			result[1] = Math.max(Math.max(x1, x2), x3);

		} else {
			// Only one real root. Return Double.MAX_VALUE to signal only one real root
			// available
			result[0] = Double.MAX_VALUE;
			
			double S = -Math.signum(R)*Math.pow(Math.abs(R) + Math.sqrt(disc), 1./3.);
			double T = Q/S;
			
			result[1] = S + T - a / 3.;
		}

		return result;
	}
}
