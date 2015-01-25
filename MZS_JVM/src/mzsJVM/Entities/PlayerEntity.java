package mzsJVM.Entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@SuppressWarnings("UnusedDeclaration")
@Entity
@Table(name="playercharacters")
public class PlayerEntity {

    @Id
    @Column(name="PlayerCharacterID")
    private int _pcID;

    @Column(name="AccountName")
    private String _accountName;
    @Column(name="CDKey")
    private String _cdKey;
    @Column(name="CharacterName")
    private String _characterName;
    @Column(name="HitPoints")
    private int _hitPoints;
    @Column(name="LocationAreaTag")
    private String _locationAreaTag;
    @Column(name="LocationX")
    private float _locationX;
    @Column(name="LocationY")
    private float _locationY;
    @Column(name="LocationZ")
    private float _locationZ;
    @Column(name="LocationOrientation")
    private float _locationOrientation;
    @Column(name="CreateTimestamp")
    private Date _createTimestamp;

    public PlayerEntity()
    {

    }


    public int getPCID() {
        return _pcID;
    }

    public void setPCID(int _pcID) {
        this._pcID = _pcID;
    }

    public String getAccountName() {
        return _accountName;
    }

    public void setAccountName(String _accountName) {
        this._accountName = _accountName;
    }

    public String getCDKey() {
        return _cdKey;
    }

    public void setCDKey(String _cdKey) {
        this._cdKey = _cdKey;
    }

    public String getCharacterName() {
        return _characterName;
    }

    public void setCharacterName(String _characterName) {
        this._characterName = _characterName;
    }

    public int getHitPoints() {
        return _hitPoints;
    }

    public void setHitPoints(int _hitPoints) {
        this._hitPoints = _hitPoints;
    }

    public String getLocationAreaTag() {
        return _locationAreaTag;
    }

    public void setLocationAreaTag(String _locationAreaTag) {
        this._locationAreaTag = _locationAreaTag;
    }

    public float getLocationX() {
        return _locationX;
    }

    public void setLocationX(float _locationX) {
        this._locationX = _locationX;
    }

    public float getLocationY() {
        return _locationY;
    }

    public void setLocationY(float _locationY) {
        this._locationY = _locationY;
    }

    public float getLocationZ() {
        return _locationZ;
    }

    public void setLocationZ(float _locationZ) {
        this._locationZ = _locationZ;
    }

    public float getLocationOrientation() {
        return _locationOrientation;
    }

    public void setLocationOrientation(float _locationOrientation) {
        this._locationOrientation = _locationOrientation;
    }

    public Date getCreateTimestamp() {
        return _createTimestamp;
    }

    public void setCreateTimestamp(Date _createTimestamp) {
        this._createTimestamp = _createTimestamp;
    }
}
