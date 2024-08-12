package example.com.noticeModel

interface NoticeRepository {
    suspend fun allNotice(): List<Notice>
    suspend fun addNotice(notice: Notice)
}