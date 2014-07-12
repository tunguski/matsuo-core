package pl.matsuo.core.model.user;

/**
 * Most general group definitions.
 * Created by tunguski on 22.12.13.
 */
public enum GroupEnum {
  /**
   * System IT administrator. Permissions checking returns true by default.
   */
  ADMIN,
  /**
   * User that has full access to company's data.
   */
  SUPERVISOR,
  /**
   * Logged user. Added by default to all logged users. It's not recommended to save it in db.
   */
  USER,
  /**
   * Not logged user.
   */
  GUEST
}
