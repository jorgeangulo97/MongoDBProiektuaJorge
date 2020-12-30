/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import java.util.Objects;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 *
 * @author angulo.jorge
 */
public final class Student implements Comparable {
    @BsonProperty(value = "_id")
    private int id;
    @BsonProperty(value = "name")
    private String name;
    private List<Score> scores;
    private double avgScore;

    public Student() {
    }

    public Student(int id, String name, List<Score> scores) {
        this.id = id;
        this.name = name;
        this.scores = scores;
        this.avgScore = this.calculateScoreAvg();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }
    
    @Override
    public String toString() {
        return "Student --> " + "ID=" + id + ", Name=" + name + ", Scores=" + scores + ", Avg Score=" + avgScore;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + Objects.hashCode(this.scores);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.scores, other.scores)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Object student) {
        Student otherStudent = (Student) student;
        
        double diff = (this.getAvgScore() - otherStudent.getAvgScore());
        
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public double calculateScoreAvg() {
        double sum = 0;
        
        for (Score score : this.scores) {
            sum += score.getScore();
        }
        
        return sum / this.scores.size();
    }
}
