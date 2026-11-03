create table if not exists Cameras(
    id serial primary key,
    camera_id uuid unique not null,
    camera_name text unique not null,
    camera_type text not null,
    location text not null,
    ip_address text not null,
    version text not null,
    status text not null check (status in ('online', 'offline')) default 'offline',
    installation_date timestamp with time zone default now()
)