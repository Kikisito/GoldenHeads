# Contributing Guidelines

Thank you for your interest in contributing! We welcome all pull requests (PRs). Please read these guidelines carefully to ensure a smooth contribution process.

## How to Contribute

- **Fork** the repository and create your branch from `main`.
- **Make your changes** in a logical, isolated commit.
- **Ensure your commit messages follow the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) specification.**
- **Push** your branch to your fork.
- **Open a Pull Request (PR)** against the `main` branch.
- All PRs **must pass CI checks** before merging.

## Pull Request Workflow

1. **Discuss**: For large or non-trivial changes, open an issue first to discuss your approach with maintainers.
2. **Branch**: Work in a feature branch. Name it descriptively (e.g., `feat/add-login-form`).
3. **Commit**: Write clear, Conventional Commit messages (see below).
4. **Test**: Add or update tests as needed.
5. **CI**: All PRs are automatically checked by our CI pipeline at [GoldenHeads CI](https://ci.redactado.com/job/GoldenHeads/). Ensure your PR passes before requesting review.
6. **Review**: Address any feedback from reviewers promptly.

## Commit Message Guidelines (Conventional Commits)

All commits **must** follow the Conventional Commits format. This is mandatory and enforced by CI.

**Commit message structure:**
```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### Common Types

| Type      | Description                                      | Example                                 |
|-----------|--------------------------------------------------|-----------------------------------------|
| feat      | A new feature                                    | `feat(auth): add login functionality`   |
| fix       | A bug fix                                        | `fix(api): handle null response`        |
| docs      | Documentation only changes                       | `docs: update README`                   |
| style     | Code style changes (formatting, etc.)            | `style: format code`                    |
| refactor  | Code refactoring (no feature or fix)             | `refactor(core): simplify logic`        |
| perf      | Performance improvements                         | `perf: optimize query`                  |
| test      | Adding or updating tests                         | `test(ui): add login tests`             |
| build     | Build system or dependency changes               | `build: update dependencies`            |
| ci        | CI configuration changes                         | `ci: update pipeline config`            |
| chore     | Miscellaneous tasks                              | `chore: update .gitignore`              |
| revert    | Revert a previous commit                         | `revert: fix(api): handle null response`|

### Examples

- `feat(login): add OAuth2 support`
- `fix(list): correct item count`
- `docs: add contribution guidelines`
- `refactor: simplify user validation logic`
- `build: upgrade to Node 18`
- `ci: add lint step to workflow`
- `chore: remove unused files`
- `feat!: remove deprecated API` *(breaking change, note the `!`)*

#### Breaking Changes

To indicate a breaking change, add `!` after the type or scope, and/or include a `BREAKING CHANGE:` footer in the body.

**Example:**
```
feat(api)!: remove deprecated endpoint

BREAKING CHANGE: The `/v1/old-endpoint` has been removed. Update your integrations to use `/v1/new-endpoint`.
```

## CI Integration

All PRs are automatically checked by our [GoldenHeads CI](https://ci.redactado.com/job/GoldenHeads/). The CI verifies:

- Linting and formatting
- Tests (unit/integration)
- Commit message compliance (Conventional Commits)

**PRs that fail CI will not be merged.** Please check the CI logs and address any issues before requesting a review.

## Additional Tips

- Keep PRs focused and minimal—one feature or fix per PR.
- Update documentation and tests as needed.
- Be responsive to reviewer feedback.

Thank you for helping us improve this project!
