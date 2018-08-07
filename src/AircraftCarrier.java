import java.util.Arrays;
import java.util.List;

public class AircraftCarrier extends Ship
{
    /**
     * Construct an AircraftCarrier with a length
     * of 5 and the specified Locations.
     *
     * @param locations
     */
    public AircraftCarrier(Location... locations)
    {
        super(5);
        addLocation(locations);
    }
}
