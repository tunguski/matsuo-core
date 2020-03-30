package pl.matsuo.core.web.controller.user;

import pl.matsuo.core.params.IRequestParams;

public interface IBlockUserParams extends IRequestParams {

  Integer getId();

  void setId(Integer id);

  Boolean getBlock();

  void setBlock(Boolean block);
}
