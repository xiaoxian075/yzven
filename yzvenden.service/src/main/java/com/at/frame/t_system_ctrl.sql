create table t_system_ctrl(
  id            int           auto_increment    comment '控制器Id',
  ctrl_name     varchar(256)  not null          comment '控制器名称',
  ctrl_id       int           not null          comment '控制器访问地址',
  ctrl_desc     varchar(64)   not null          comment '控制器描述',
  ctrl_version  int           not null          comment '版本号',
  create_time   TIMESTAMP     not null comment '创建时间',
  primary key(id)
) comment = '系统控制器列表';
ALTER TABLE `t_system_ctrl`
ADD UNIQUE INDEX `ctrl_id_unq` (`ctrl_id` ASC);