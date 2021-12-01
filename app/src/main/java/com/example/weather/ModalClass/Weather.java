package com.example.weather.ModalClass;

public class Weather {
    String date, minTemp, maxTemp, sunRise, sunSet, moonRise, moonSet,
            wind, uv, rainProb, dayPhrase, nightPhrase;

    public String getDayPhrase() {
        return dayPhrase;
    }

    public void setDayPhrase(String dayPhrase) {
        this.dayPhrase = dayPhrase;
    }

    public String getNightPhrase() {
        return nightPhrase;
    }

    public void setNightPhrase(String nightPhrase) {
        this.nightPhrase = nightPhrase;
    }

    public Weather(String date, String minTemp, String maxTemp, String sunRise, String sunSet, String moonRise, String moonSet, String wind, String uv, String rainProb, String dayPhrase, String nightPhrase) {
        this.date = date;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.moonRise = moonRise;
        this.moonSet = moonSet;
        this.wind = wind;
        this.uv = uv;
        this.rainProb = rainProb;
        this.dayPhrase = dayPhrase;
        this.nightPhrase = nightPhrase;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public void setSunSet(String sunSet) {
        this.sunSet = sunSet;
    }

    public String getMoonRise() {
        return moonRise;
    }

    public void setMoonRise(String moonRise) {
        this.moonRise = moonRise;
    }

    public String getMoonSet() {
        return moonSet;
    }

    public void setMoonSet(String moonSet) {
        this.moonSet = moonSet;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getRainProb() {
        return rainProb;
    }

    public void setRainProb(String rainProb) {
        this.rainProb = rainProb;
    }
}
