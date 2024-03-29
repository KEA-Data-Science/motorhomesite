package kea.motorhome.motorhomesite.enums;
// by KCN
public enum SiteRole
{
    SALES,ADMIN,BOOKKEEPER,MECHANIC,CUSTOMER,OTHER,ERROR;
    /** Retrieves a SiteRole based on content of supplied String */
    public static SiteRole from(String role){
        switch(role.toLowerCase()){
            case "sales":return SALES;
            case "admin":return ADMIN;
            case "bookkeeper":return BOOKKEEPER;
            case "mechanic":return MECHANIC;
            case "customer":return CUSTOMER;
            case "other":return OTHER;
            default:return ERROR;
        }
    }
}