package media.diletant.recommendationsbuilder.api.model.enumm;

public enum TimePeriod {
  ancientWorld("до VIII до н.э."),
  antiquity("VIII до н.э - V н.э."),
  middleAges("V-XIV"),
  renaissance("XV-XVII"),
  newTimes("XVII-XX"),
  modernTimes("XX-XXI");

  private final String romeRepresentation;

  TimePeriod(String romeRepresentation) {
    this.romeRepresentation = romeRepresentation;
  }

  public final String getRomeRepresentation() {
    return this.romeRepresentation;
  }
}
