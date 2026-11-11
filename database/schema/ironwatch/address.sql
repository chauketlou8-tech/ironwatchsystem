create table if not exists Address (
    id serial primary key,
    street text not null,
    city text not null,
    province text not null,
    country text not null,
    postal_code text,
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
);