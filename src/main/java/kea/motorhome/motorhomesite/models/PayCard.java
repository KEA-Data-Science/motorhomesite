package kea.motorhome.motorhomesite.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class PayCard {

	private int cardID;
	private String cardType;
	private String cardNumber;
	private LocalDateTime expirationDate;
	private int securityDigits;

	public PayCard()	{	}

	public PayCard(int cardID, String cardType, String cardNumber, LocalDateTime expirationDate, int securityDigits)
	{
		this.cardID = cardID;
		this.cardType = cardType;
		this.cardNumber = cardNumber;
		this.expirationDate = expirationDate;
		this.securityDigits = securityDigits;
	}

	public int getCardID()
	{
		return cardID;
	}

	public void setCardID(int cardID)
	{
		this.cardID = cardID;
	}

	public String getCardType()
	{
		return cardType;
	}

	public void setCardType(String cardType)
	{
		this.cardType = cardType;
	}

	public String getCardNumber()
	{
		return cardNumber;
	}

	public void setCardNumber(String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public LocalDateTime getExpirationDate()
	{
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate)
	{
		this.expirationDate = expirationDate;
	}

	public int getSecurityDigits()
	{
		return securityDigits;
	}

	public void setSecurityDigits(int securityDigits)
	{
		this.securityDigits = securityDigits;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PayCard)) return false;
		PayCard payCard = (PayCard)o;
		return getCardID() == payCard.getCardID() &&
			   getSecurityDigits() == payCard.getSecurityDigits() &&
			   Objects.equals(getCardType(), payCard.getCardType()) &&
			   Objects.equals(getCardNumber(), payCard.getCardNumber()) &&
			   Objects.equals(getExpirationDate(), payCard.getExpirationDate());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getCardID(), getCardType(), getCardNumber(), getExpirationDate(), getSecurityDigits());
	}

	@Override
	public String toString()
	{
		return "PayCard{" +
			   "cardID=" + cardID +
			   ", cardType='" + cardType + '\'' +
			   ", cardNumber='" + cardNumber + '\'' +
			   ", expirationDate=" + expirationDate +
			   ", securityDigits=" + securityDigits +
			   '}';
	}
}