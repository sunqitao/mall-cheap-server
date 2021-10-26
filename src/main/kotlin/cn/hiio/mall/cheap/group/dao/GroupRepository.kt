package cn.hiio.mall.cheap.group.dao

import cn.hiio.mall.cheap.group.vo.GroupVo
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : MongoRepository<GroupVo, String> {
    fun findGroupVoByParentId(parentId:String,pageable: Pageable):List<GroupVo>
}