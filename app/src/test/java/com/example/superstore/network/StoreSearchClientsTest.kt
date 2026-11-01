package com.example.superstore.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StoreSearchClientsTest {

    private val url = "https://api.github.com/repos/octocat/Hello-World/releases/latest"

    @Test
    fun `404 means the repository has no release`() {
        assertTrue(isMissingReleaseStatus(404))
        assertFalse(isMissingReleaseStatus(403))
        assertFalse(isMissingReleaseStatus(429))
        assertFalse(isMissingReleaseStatus(200))
    }

    @Test
    fun `403 and 429 explain the API limit instead of the raw status`() {
        val forbidden = httpErrorMessage(403, url)
        val tooMany = httpErrorMessage(429, url)
        assertTrue(forbidden.contains("Limite de requisições"))
        assertTrue(tooMany.contains("Limite de requisições"))
        assertFalse(forbidden.startsWith("HTTP 403"))
        assertFalse(tooMany.startsWith("HTTP 429"))
    }

    @Test
    fun `other statuses keep the raw code`() {
        assertEquals("HTTP 500 ao consultar $url", httpErrorMessage(500, url))
        assertEquals("HTTP 404 ao consultar $url", httpErrorMessage(404, url))
    }

    @Test
    fun `the status exception carries the code so callers can branch`() {
        val error = HttpStatusException(404, url)
        assertEquals(404, error.statusCode)
        assertEquals("HTTP 404 ao consultar $url", error.message)
    }
}
