package geekbrains;

class ToyCreation {
    private final int toyId;
    private final String toyName;
    private int toyDistributionFrequency;

    public ToyCreation(int toyId, String toyName, int toyDistributionFrequency) {
        this.toyId = toyId;
        this.toyName = toyName;
        this.toyDistributionFrequency = toyDistributionFrequency;
    }

    public int getToyId() {
        return toyId;
    }

    public String getToyName() {
        return toyName;
    }

    public int getToyDistributionFrequency() {
        return toyDistributionFrequency;
    }

    public void setToyDistributionFrequency(int toyDistributionFrequency) {
        this.toyDistributionFrequency = toyDistributionFrequency;
    }
}
