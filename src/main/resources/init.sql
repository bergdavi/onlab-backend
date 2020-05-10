create table game_queue
(
    user   varchar(36) not null,
    game   varchar(36) not null,
    joined datetime    not null,
    primary key (user, game)
);

create table games
(
    id            varchar(36)  not null,
    name          varchar(255) not null,
    description   text         null,
    min_players   int(5)       not null,
    max_players   int(5)       not null,
    initial_state json         not null,
    constraint games_id_uindex
        unique (id)
);

alter table games
    add primary key (id);

create table gameplays
(
    id            varchar(36)                      not null,
    next_user_idx int(5)                           not null,
    game_state    json                             not null,
    user_count    int(5)                           null,
    game          varchar(36)                      null,
    status        enum ('IN_PROGRESS', 'FINISHED') not null,
    constraint gameplays_id_uindex
        unique (id),
    constraint gameplays_games_id_fk
        foreign key (game) references games (id)
);

alter table gameplays
    add primary key (id);

create table user_details
(
    id       varchar(36) not null,
    username varchar(50) not null,
    email    varchar(50) not null,
    constraint user_details_email_uindex
        unique (email),
    constraint user_details_id_uindex
        unique (id),
    constraint user_details_username_uindex
        unique (username)
);

alter table user_details
    add primary key (id);

create table game_ratings
(
    id     varchar(36) not null,
    user   varchar(36) null,
    game   varchar(36) null,
    rating int(5)      null,
    constraint game_ratings_id_uindex
        unique (id),
    constraint game_ratings_games_id_fk
        foreign key (game) references games (id),
    constraint game_ratings_user_details_id_fk
        foreign key (user) references user_details (id)
);

alter table game_ratings
    add primary key (id);

create table user_gameplays
(
    user     varchar(36)                  not null,
    gameplay varchar(36)                  not null,
    user_idx int(5)                       not null,
    result   enum ('WIN', 'LOSE', 'DRAW') null,
    primary key (user, gameplay),
    constraint user_gameplays_gameplays_id_fk
        foreign key (gameplay) references gameplays (id),
    constraint user_gameplays_user_details_id_fk
        foreign key (user) references user_details (id)
);

create table users
(
    USERNAME varchar(50) not null
        primary key,
    PASSWORD varchar(68) not null,
    ENABLED  tinyint(1)  not null,
    constraint users_user_details_username_fk
        foreign key (USERNAME) references user_details (username)
);

create table authorities
(
    USERNAME  varchar(50) not null,
    AUTHORITY varchar(68) not null,
    constraint authorities_ibfk_1
        foreign key (USERNAME) references users (USERNAME)
);

create index USERNAME
    on authorities (USERNAME);

