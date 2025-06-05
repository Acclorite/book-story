/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.permission

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.repository.PermissionRepository
import javax.inject.Inject

class ReleasePersistableUriPermissionUseCase @Inject constructor(
    private val permissionRepository: PermissionRepository
) {

    suspend operator fun invoke(uri: String) {
        logI("Releasing persistable URI permission from \"$uri\".")

        permissionRepository.releasePersistableUriPermission(uri).fold(
            onSuccess = {
                logI("Successfully released persistable URI permission from \"$uri\".")
            },
            onFailure = {
                logE("Could not release persistable URI permission from \"$uri\" with error: ${it.message}.")
            }
        )
    }
}