package gehtsoft.ballisticcalculator.Tools;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TestTrajectoryLoader {
    @Test
    public void loads() throws IOException {
        TrajectoryLoader loader = new TrajectoryLoader();
        loader.load("g1_twist.txt", null);
        assertThat(loader.getProjectile()).isNotNull();
        assertThat(loader.getAtmosphere()).isNotNull();
        assertThat(loader.getWeapon()).isNotNull();
        assertThat(loader.getWind()).isNotNull();
        
        assertThat(loader.getTrajectory())
            .isNotNull();
        assertThat(loader.getTrajectory().size())
            .isEqualTo(21);


    }
}
