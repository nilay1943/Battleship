import java.util.*;

public abstract class Ship
{
    private int length;
    private List<Location> locations;
    private List<Location> hitsTaken;

    public Ship(int length)
    {
        locations = new ArrayList<Location>();
        hitsTaken = new ArrayList<Location>();
        this.length = length;
    }

    public void addLocation(Location... loc)
    {
        List<Location> locs = Arrays.asList(loc);//makes loc a list
        locations.addAll(locs);
    }

    public List<Location> getLocations()
    {
        return locations;
    }

    /**
     * Add Location loc to hitsTaken.
     *
     * @param loc
     */
    public void takeHit(Location loc)
    {
        if(locations.contains(loc))
            hitsTaken.add(loc);

    }

    /**
     * Returns true if the number of hits taken is
     *   equal to the length of this ship.
     *
     * @return
     */
    public boolean isSunk()
    {
        return hitsTaken.size() == length;

    }
}
