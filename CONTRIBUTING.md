# Contributing to this project

## Conventions

This project uses [NullAway](https://github.com/uber/NullAway). All parameters, return types and fields that are
nullable need to be annotated with `@Nullable`. Everything that is not annotated
is expected to be non-null. It is recommended to avoid any nullable types.

## Opening a Pull Request

A Pull Request should **always** only modify one aspect.

Feature, Performance and Refactor Pull Requests are meant to target the `develop` branch.
Chore and Bug Fix Pull Requests are meant to target the `master` branch.

The title of your pull request should start with its type: 
- `feature:` for features 
- `bug:` for bug fixes
- `perf:` for performance improvements
- `chore:` for changes to GitHub Actions, the project setup (gradle), etc.
- `refactor:` for general code improvements

Your Pull Request will be reviewed and merged once it is reasonable. All PRs will be squashed.

### Feature PR

When adding a new feature, you should make sure that it is well-tested and covered by tests.

### Bug fix PR

If you're fixing a bug, you should also add a test case that covers it.
That way the bug won't be reintroduced without noticing.

### Other PRs

All other types of pull requests are welcome too. Additional measures 
(like adding a benchmark for a performance improvement) can be discussed in an open pull request.
