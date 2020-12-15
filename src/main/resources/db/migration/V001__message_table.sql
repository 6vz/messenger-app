create table message (
  id                   uuid primary key,
  sender               varchar not null,
  recipient            varchar not null,
  payload              varchar not null
);
