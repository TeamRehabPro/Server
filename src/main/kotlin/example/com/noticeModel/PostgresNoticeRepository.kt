package example.com.noticeModel

import example.com.db.NoticeDAO
import example.com.db.noticeDaoToModel
import example.com.db.noticeSuspendTransaction

class PostgresNoticeRepository : NoticeRepository {
    override suspend fun allNotice(): List<Notice> = noticeSuspendTransaction {
        NoticeDAO.all().map(::noticeDaoToModel)
    }

    override suspend fun addNotice(notice: Notice): Unit = noticeSuspendTransaction {
        NoticeDAO.new {
            employeeId = notice.employeeId
            creationDate = notice.creationDate
            title = notice.title
            content = notice.content
        }
    }
}
