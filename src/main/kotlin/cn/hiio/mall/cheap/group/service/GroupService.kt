package cn.hiio.mall.cheap.group.service

import cn.hiio.mall.cheap.common.enums.StatusCode
import cn.hiio.mall.cheap.common.utils.AssertUtil
import cn.hiio.mall.cheap.group.dao.GroupRepository
import cn.hiio.mall.cheap.group.vo.GroupVo
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import javax.annotation.Resource

@Service
class GroupService : Logging {
    @Autowired
    lateinit var groupRepository: GroupRepository

    @Resource
    lateinit var redisTemplate : RedisTemplate<String, GroupVo>

    fun queryList(parentId: String,pageNum:Int,pageSize:Int): List<GroupVo> {
        AssertUtil.isNotNull(parentId,StatusCode.failed,"上级id不能为空")
        var pageRequest = PageRequest.of(pageNum,pageSize,Sort.by(Sort.Direction.DESC,"created"))
        return groupRepository.findGroupVoByParentId(parentId,pageRequest)
    }

}