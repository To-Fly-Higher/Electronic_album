-- ============================================
-- 2. 相册类别表（album_category）
-- ============================================
CREATE TABLE album_category (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL
);

-- ============================================
-- 3. 相册表（album）
-- ============================================
CREATE TABLE album (
    id           SERIAL PRIMARY KEY,
    user_id      INTEGER NOT NULL,
    category_id  INTEGER,
    name         VARCHAR(100) NOT NULL,
    cover_url    VARCHAR(255),
    remark       VARCHAR(255),
    is_public    SMALLINT NOT NULL DEFAULT 0, -- 1公开/0私密

    CONSTRAINT fk_album_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_album_category
        FOREIGN KEY (category_id) REFERENCES album_category(id)
);

-- ============================================
-- 4. 图片表（photo）
-- ============================================
CREATE TABLE photo (
    id        SERIAL PRIMARY KEY,
    user_id   INTEGER NOT NULL,
    url       VARCHAR(255) NOT NULL,
    name      VARCHAR(100) NOT NULL,

    CONSTRAINT fk_photo_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ============================================
-- 5. 相册图片关联表（album_photo）
-- ============================================
CREATE TABLE album_photo (
    id        SERIAL PRIMARY KEY,
    album_id  INTEGER NOT NULL,
    photo_id  INTEGER NOT NULL,

    CONSTRAINT fk_album_photo_album
        FOREIGN KEY (album_id) REFERENCES album(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_album_photo_photo
        FOREIGN KEY (photo_id) REFERENCES photo(id)
        ON DELETE CASCADE
);

-- ============================================
-- 6. 评论表（comment）
-- ============================================
CREATE TABLE comment (
    id           SERIAL PRIMARY KEY,
    photo_id     INTEGER NOT NULL,
    user_id      INTEGER NOT NULL,
    content      VARCHAR(255) NOT NULL,

    CONSTRAINT fk_comment_photo
        FOREIGN KEY (photo_id) REFERENCES photo(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ============================================
-- 7. 点赞表（photo_like）
-- ============================================
CREATE TABLE photo_like (
    id        SERIAL PRIMARY KEY,
    photo_id  INTEGER NOT NULL,
    user_id   INTEGER NOT NULL,

    CONSTRAINT fk_like_photo
        FOREIGN KEY (photo_id) REFERENCES photo(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_like_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    
    -- 同一用户对同一图片只能点一次赞
    CONSTRAINT uq_photo_like UNIQUE (photo_id, user_id)
);

-- ============================================
-- 8. 好友请求表（friend_request）
-- ============================================
CREATE TABLE friend_request (
    id            SERIAL PRIMARY KEY,
    from_user_id  INTEGER NOT NULL,
    to_user_id    INTEGER NOT NULL,
    status        SMALLINT NOT NULL DEFAULT 0, -- 0待处理/1同意/2拒绝/3撤回
    remark        VARCHAR(255),

    CONSTRAINT fk_friend_request_from
        FOREIGN KEY (from_user_id) REFERENCES users(id),

    CONSTRAINT fk_friend_request_to
        FOREIGN KEY (to_user_id) REFERENCES users(id)
);

-- ============================================
-- 9. 好友关系表（friend）
-- ============================================
CREATE TABLE friend (
    id           SERIAL PRIMARY KEY,
    user_id      INTEGER NOT NULL,
    friend_id    INTEGER NOT NULL,

    CONSTRAINT fk_friend_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_friend_friend
        FOREIGN KEY (friend_id) REFERENCES users(id),

    -- 防止重复好友关系
    CONSTRAINT uq_friend UNIQUE (user_id, friend_id)
);
