package cn.arorms.android.ht.server.pojo.entity

import cn.arorms.android.ht.server.pojo.enums.DialogueType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_dialogue")
data class ChatDialogue(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "dialogue_type")
    var dialogueType: DialogueType,

    var title: String? = null,

    @OneToMany(mappedBy = "dialogue", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var participants: MutableList<ChatDialogueParticipant> = mutableListOf(),

    @OneToMany(mappedBy = "dialogue", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var messages: MutableList<ChatMessage> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "last_message_content")
    var lastMessageContent: String? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
