create table t_system_ctrl_method(
  id          int           auto_increment  comment '方法Id',
  ctrl_id     int           not null        comment 't_system_ctrl.id',
  method_name varchar(64)   not null        comment '方法名称',
  method_id   int           not null        comment '方法Id',
  method_desc varchar(64)   not null        comment '方法描述',
  method_version int        not null        comment '方法版本',
  create_time   TIMESTAMP   not null        comment '创建时间',
  key `ctrl_id_pk`(ctrl_id),
  primary key(id)
) comment = '系统控制器方法列表';
ALTER TABLE `t_system_ctrl_method`
ADD UNIQUE INDEX `method_id_unq` (`method_id` ASC,`ctrl_id` ASC);
