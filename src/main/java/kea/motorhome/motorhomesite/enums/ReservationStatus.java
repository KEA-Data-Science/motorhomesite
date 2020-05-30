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
        String lowerCaseStatus = statusString.toLowerCase();

        switch(lowerCaseStatus)
        {
            case "initialized":
                return Initialized;
            case "accepted":
                return Accepted;
            case "ongoing":
                return Ongoing;
            case "sealed":
                return Sealed;
            case "finalized":
                return Finalized;
            default:
                return Error;
        }
    }
}
