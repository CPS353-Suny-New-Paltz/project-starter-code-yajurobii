package project.annotations;

public class Delimiter {
  private String combodelimiter; // ";"
  private String resultdelimiter; // ":"

  public Delimiter(String combodelimiter, String resultdelimiter) {
    this.combodelimiter = combodelimiter;
    this.resultdelimiter = resultdelimiter;
  }

  public String getComboDelimiter() {
    return combodelimiter;
  }

  public String getResultDelimiter() {
    return resultdelimiter;
  }
}
