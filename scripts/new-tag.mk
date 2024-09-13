.PHONY: New Tag

# Variables
TAG_NAME = $(tag)
TAG_MESSAGE = "Release version $(tag)"

MAKEFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
MAKEFILE_DIR := $(dir $(MAKEFILE_PATH))

new-tag:
	@echo "Running the shell command..."

	@sh "$(MAKEFILE_DIR)/release_notes.sh"

	@echo "Creating Git tag $(TAG_NAME)..."
	@git tag -a $(TAG_NAME) -m $(TAG_MESSAGE)

	@echo "Pushing tag $(TAG_NAME) to remote repository..."
	@git push origin $(TAG_NAME)

	@echo "Release process completed successfully."
