import java.util.Arrays;

public final class Perceptron {

    private double alpha;
    private double theta;
    private double[] weights;

    public Perceptron() {
        alpha = getRandomDouble();
        theta = getRandomDouble();
        weights = getRandomWeights();
    }

    public int calculate(Observation observation) {
        double net = theta;
        for (int i = 0; i < Observation.ARRAY_LENGTH; i++)
            net += weights[i] * observation.getCountedLetters()[i];
        if(net < 0)
            return 0;
        return 1;
    }

    public void training(String assumed, String real, Observation observation) {
        int y, d, delta;
        y = calculate(observation);
        d = real.equals(assumed) ? 1 : 0;
        delta = d - y;
        for (int i = 0; i < weights.length; i++)
            weights[i] += alpha * delta * observation.getCountedLetters()[i];
        theta += delta * alpha;
    }

    private double getRandomDouble(){
        return Math.random();
    }

    private double[] getRandomWeights() {
        double[] array = new double[Observation.ARRAY_LENGTH];
        double random = (Math.random() * 2) - 1;
        Arrays.fill(array, random);
        return array;
    }

}