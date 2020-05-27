package kea.motorhome.motorhomesite.enums;
// by KCN
/**
 * Enum signals the status of a reservation in the system.
 * A single static method is included to convert at string to a status with ease.
 */
public enum ReservationStatus
{
    Initialized, Accepted, Ongoing, Sealed, Finalized, Error; // todo: description of enum states in artifacts

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
            case "Sealed":
                return Sealed;
            case "Finalized":
                return Finalized;
            default:
                return Error;
        }
    }
}
