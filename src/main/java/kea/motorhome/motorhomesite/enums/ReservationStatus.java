package kea.motorhome.motorhomesite.enums;

/**
 * Enum signals the status of a reservation in the system.
 * A single static method is included to convert at string to a status with ease.
 */
public enum ReservationStatus
{
    Initialized, Accepted, Ongoing, Finalized, Error;

    public static ReservationStatus status(String statusString)
    {
        switch(statusString)
        {
            case "Initialized":
                return Initialized;
            case "Accepted":
                return Accepted;
            case "Ongoing":
                return Ongoing;
            case "Finalized":
                return Finalized;
            default:
                return Error;
        }
    }
}
