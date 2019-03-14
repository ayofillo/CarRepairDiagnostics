package com.ubiquisoft.evaluation.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Car {

    private String year;
    private String make;
    private String model;

    private List<Part> parts;

    public Map<PartType, Integer> getMissingPartsMap() {
        /*
         * Return map of the part types missing.
         *
         * Each car requires one of each of the following types:
         *      ENGINE, ELECTRICAL, FUEL_FILTER, OIL_FILTER
         * and four of the type: TIRE
         *
         * Example: a car only missing three of the four tires should return a map like this:
         *
         *      {
         *          "TIRE": 3
         *      }
         */


        List<PartType> matrix = new ArrayList<>();
        AtomicInteger tireCount = new AtomicInteger(4);
        this.getParts().parallelStream().forEach(part -> {
            PartType pt = part.getType();
            if (pt.equals(PartType.TIRE)) {
                tireCount.getAndDecrement();
            }
            matrix.add(part.getType());
        });

        Map<PartType, Integer> missingParts = new HashMap<>();
        //if greater, then a/some tire(s) is missing
        if (tireCount.get() > 0) {
            missingParts.put(PartType.TIRE, tireCount.intValue());
        }
        //check if any part is not in the matrix
        Arrays.stream(PartType.values()).parallel().forEach(pt -> {
            if (!matrix.contains(pt)) missingParts.put(pt, 1);
        });

        return missingParts;
    }

    @Override
    public String toString() {
        return "Car{" +
                "year='" + year + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", parts=" + parts +
                '}';
    }

    /* --------------------------------------------------------------------------------------------------------------- */
    /*  Getters and Setters *///region
    /* --------------------------------------------------------------------------------------------------------------- */

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    /* --------------------------------------------------------------------------------------------------------------- */
    /*  Getters and Setters End *///endregion
    /* --------------------------------------------------------------------------------------------------------------- */

}
