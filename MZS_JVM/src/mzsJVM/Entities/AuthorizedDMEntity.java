package mzsJVM.Entities;

import java.io.Serializable;

@SuppressWarnings("UnusedDeclaration")
public class AuthorizedDMEntity implements Serializable {
    private int _authorizedDMID;
    private String _name;
    private String _cdKey;
    private int _dmRole;
    private boolean _isActive;


    public AuthorizedDMEntity(int authorizedDMID,
                              String name,
                              String cdKey,
                              int dmRole,
                              boolean isActive)
    {
        _authorizedDMID = authorizedDMID;
        _name = name;
        _cdKey = cdKey;
        _dmRole = dmRole;
        _isActive = isActive;
    }


    public int getDMRole() {
        return _dmRole;
    }

    public void setDMRole(int _dmRole) {
        this._dmRole = _dmRole;
    }

    public int getAuthorizedDMID() {
        return _authorizedDMID;
    }

    public void setAuthorizedDMID(int _authorizedDMID) {
        this._authorizedDMID = _authorizedDMID;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getCDKey() {
        return _cdKey;
    }

    public void setCDKey(String _cdKey) {
        this._cdKey = _cdKey;
    }

    public boolean getIsActive() {
        return _isActive;
    }

    public void setIsActive(boolean _isActive) {
        this._isActive = _isActive;
    }
}
