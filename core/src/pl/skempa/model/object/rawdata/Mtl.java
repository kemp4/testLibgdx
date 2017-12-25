package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by szymk on 12/24/2017.
 */

public class Mtl {

    private String name;
    private float ns ;// shininess factor?
    private Vector3 ka ;//ambient
    private Vector3 kd ;//diffuse
    private Vector3 ks ;//specular
    private Vector3 ke ;//emmision
    private float ni ;
    private float d ;
    private int illum ;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNs() {
        return ns;
    }

    public void setNs(float ns) {
        this.ns = ns;
    }

    public Vector3 getKa() {
        return ka;
    }

    public void setKa(Vector3 ka) {
        this.ka = ka;
    }

    public Vector3 getKd() {
        return kd;
    }

    public void setKd(Vector3 kd) {
        this.kd = kd;
    }

    public Vector3 getKs() {
        return ks;
    }

    public void setKs(Vector3 ks) {
        this.ks = ks;
    }

    public Vector3 getKe() {
        return ke;
    }

    public void setKe(Vector3 ke) {
        this.ke = ke;
    }

    public float getNi() {
        return ni;
    }

    public void setNi(float ni) {
        this.ni = ni;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public int getIllum() {
        return illum;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }


}
