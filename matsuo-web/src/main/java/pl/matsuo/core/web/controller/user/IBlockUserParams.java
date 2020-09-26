package pl.matsuo.core.web.controller.user;

import pl.matsuo.core.params.IRequestParams;

public interface IBlockUserParams extends IRequestParams {

  Long getId();

  void setId(Long id);

  Boolean getBlock();

  void setBlock(Boolean block);
}
