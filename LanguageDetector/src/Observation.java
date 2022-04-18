
public final class Observation {

    public static final int ARRAY_LENGTH = 26;

    private double[] countedLetters;
    private String language;

    public Observation(String text) {
        this.countedLetters = getCounted(text);
    }

    public Observation(String text, String language) {
        this.countedLetters = getCounted(text);
        this.language = language;
    }

    private double[] getCounted(String text) {
        double[] letters = new double[ARRAY_LENGTH];
        for (int iteration = 'a', lettersIndex = 0; iteration <= 'z'; iteration++) {
            char letter = (char) iteration;
            double quantity = (double) text.chars()
                    .filter(character -> character == letter)
                    .count();
            letters[lettersIndex++] = quantity;
        }
        double euclideanDistance = getEuclideanDistance(letters);
        double[] counted = new double[ARRAY_LENGTH];
        for (int i = 0; i < letters.length; i++)
            counted[i] = letters[i]/euclideanDistance;
        return counted;
    }

    private double getEuclideanDistance(double[] letters) {
        double counter = 0;
        for (int i = 0; i < letters.length; i++)
            counter += Math.pow(letters[i], 2);
        return Math.sqrt(counter);
    }

    public String getLanguage() { return language; }

    public double[] getCountedLetters() { return countedLetters; }

}