package mn.enkhsanaa.model;

import java.io.Serializable;

/**
 * Created by Enkhsanaa Natsagodrj on 11/28/2017.
 * Option class
 */

public class Option implements Serializable {
    private static final long serialVersionUID = 1L;
    private String option;

    public Option(String option) {
        this.option = option.trim();
    }

    @Override
    public String toString() { return option; }

}