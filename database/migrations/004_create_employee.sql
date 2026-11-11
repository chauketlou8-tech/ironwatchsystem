create table if not exists Employees(
    id serial primary key,
    firstname text not null,
    lastname text not null,
    email text not null unique,
    date_of_birth date not null check (date_of_birth <= current_date - interval '18 years'),
    salary numeric(10, 2) not null check (salary > 0),
    hire_date date not null,
    department text not null,
    position text not null,
    phone_number text not null,
    gender gender not null,
    address_id int references address(id) on delete set null,
    profile_picture_path text not null,
    created_at timestamp with time zone default now(),
    updated_at timestamp with time zone default now()
)