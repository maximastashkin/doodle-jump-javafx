import javafx.geometry.Point2D;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.doodle_game.physic.Collider;

public class CollisionTest {

    @Test
    public void firstTest() {
        Collider firstCollider = new Collider(new Point2D(0, 0), new Point2D(100, 100));
        Collider secondCollider = new Collider(new Point2D(0, 200), new Point2D(150, 150));
        Assertions.assertTrue(firstCollider.isCollideWith(secondCollider));
    }
}