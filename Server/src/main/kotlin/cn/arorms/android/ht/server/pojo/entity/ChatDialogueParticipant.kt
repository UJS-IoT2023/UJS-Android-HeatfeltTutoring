package cn.arorms.android.ht.server.pojo.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "chat_dialogue_participant")
data class ChatDialogueParticipant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialogue_id")
    var dialogue: ChatDialogue,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_user_id")
    var participantUser: User,
    
    var joinAt: LocalDateTime = LocalDateTime.now(),
)
