create table if not exists Users(
    id serial primary key,
    firstname text not null,
    lastname text not null,
    email text not null unique,
    password text not null,
    position text not null,
    profile_picture_path text not null,
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
)