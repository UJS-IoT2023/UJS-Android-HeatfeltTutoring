# 服务器端开发

## 基础功能

### POJO 定义

**实体类**

通过 Spring JPA 集成 Hibernate 采用 DDL 语言定义数据库进行数据库定义，并利用注解对数据表进行调整。

```kotlin
@Entity @Table(name = "users")
data class User(

    // ===== Auth information =====
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", unique = true)
    var username: String,

    @Column(unique = true)
    var email: String,

    var password: String,
    
    // ===== Extended info =====
    @Enumerated(EnumType.STRING)
    var role: Role? = Role.STUDENT,
    
    @Column(name = "google_id")
    var googleId: String? = null,

    @OneToOne
    @JoinColumn(name = "teacher_profile_id")
    var teacherProfile: TeacherProfile? = null,

    @OneToOne @JoinColumn(name = "wallet_id")
    var wallet: Wallet? = null,
    
    // ===== Profile information =====
    @Column(name = "phone_number", unique = true, length = 20)
    var phoneNumber: String? = null,

    @Column(name = "avatar_url")
    var avatarUrl: String? = null,

    @Column(name = "real_name")
    var realName: String? = null,
    
    var gender: String? = null,

    @Column(name = "wechat_id", unique = true)
    var wechatId: String? = null,
    
    @Column(name =  "qq_id", unique = true)
    var qqId: String? = null,
    
    var address: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        @EntityGraph(attributePaths = ["id", "username"])
        @JvmStatic
        fun basicProjection() = this
    }
}
```


**枚举类**

在设计用户类别时，设定了多个用户角色来区分不同身份。

> 这里的枚举类包用 enums，避免与原来的 enum 包冲突。

```kotlin
package cn.arorms.android.ht.server.enums

enum class Role {
    STUDENT,
    PARENT,
    TEACHER,
    ADMIN,
}
```

其中，枚举类与数据库的对应默认是数字，为了方便维护和易读性，手动定义成字符串枚举类

```kotlin
@Enumerated(EnumType.STRING)
var role: Role? = Role.STUDENT,
```

**外键**

## 服务器安全

### 基于 JWT 的无状态验证



### Oauth2 联合认证

