package solids;

import transforms.Point3D;

import java.util.List;

public interface Solid {

    //Seznam bodu objektu
    List<Point3D> getVerticies();

    //indexy bodu spolecne propojenych
    List<Integer> getIndicies();
}
