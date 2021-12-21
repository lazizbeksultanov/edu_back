package uz.gvs.admin_crm.entity.enums;

import java.util.List;

public enum WeekdayName {
    MONDAY("Dush"), TUESDAY("Sesh"), WEDNESDAY("Chor"), THURSDAY("Pay"), FRIDAY("Ju"), SATURDAY("Shan"), SUNDAY("Yak");
    public String name;

    WeekdayName(String name) {
        this.name = name;
    }
}
