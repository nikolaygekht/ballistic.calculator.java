package gehtsoft.ballisticcalculator;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class TestVector {
    @Test
    void Constructor() {
        Vector v = new Vector(1, 2, 3);
        assertThat(v.getX()).isEqualTo(1);
        assertThat(v.getY()).isEqualTo(2);
        assertThat(v.getZ()).isEqualTo(3);
    }    

    @Test
    void normalize() {
        Vector v = new Vector(1, 2, 3);
        assertThat(v.normalize().getMagnitude()).isLessThanOrEqualTo(1);

        v = new Vector(1234, 2345, 3567);
        assertThat(v.normalize().getMagnitude()).isLessThanOrEqualTo(1);
    }

    @Test 
    void getMagnitude() {
        Vector v = new Vector(1, 2, 3);
        assertThat(v.getMagnitude()).isEqualTo(3.7416573867739413);
    }

    @Test
    void multiply() {
        Vector v = new Vector(1, 2, 3);
        v = v.mul(2);
        assertThat(v.getX()).isEqualTo(2);
        assertThat(v.getY()).isEqualTo(4);
        assertThat(v.getZ()).isEqualTo(6);
    }

    @Test
    void add() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(2, 3, 4);
        Vector v = v1.add(v2);
        assertThat(v.getX()).isEqualTo(3);
        assertThat(v.getY()).isEqualTo(5);
        assertThat(v.getZ()).isEqualTo(7);
    }

    @Test
    void subtract() {
        Vector v1 = new Vector(5, 2, 9);
        Vector v2 = new Vector(2, 3, 4);
        Vector v = v1.subtract(v2);
        assertThat(v.getX()).isEqualTo(3);
        assertThat(v.getY()).isEqualTo(-1);
        assertThat(v.getZ()).isEqualTo(5);
    }

}
