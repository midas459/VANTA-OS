## Project overview

**VANTA-OS** is an Android-hosted personal computing environment designed first for a Samsung Galaxy S10e. It is not intended to replace Android's operating system or bypass its security model. Instead, VANTA aims to provide a focused, touch-first workspace that organizes tools, projects, information, and supported phone integrations behind a consistent interface.

Its primary experience is **THE VEIL**, with a dark, restrained visual identity and amber accents. The planned architecture separates responsibilities into named modules: SENTIENT CORE for orchestration, DIRECTIVE for missions and tasks, MNEMOS for knowledge and memory, INFINITE FABRIC for workspace resources, MIRROR for isolated virtual environments, GHOSTLINE for supported Android integrations, ORACLE for AI provider routing, WARDEN for permissions and safety, BLACKBOX for event history, RECALL for checkpoints, and EVOLUTION for controlled module development.

A central design goal is a persistent workspace that feels larger and more organized than a conventional collection of phone apps. The proposed 200 GB workspace is a logical capacity target, not a claim that software can create physical storage. Actual usable space depends on the device's storage, removable media, filesystem limits, and user-approved allocation. VANTA must report RAM, flash storage, cache, virtual memory, and remote storage accurately rather than presenting them as interchangeable.

The project follows a free-first and offline-capable direction. Networked AI providers, cloud services, and Supabase-backed capabilities should be optional, clearly configured, and used only with the user's consent. The interface should include a way to connect additional Supabase projects without exposing service-role secrets in the Android client.

This repository is a reconstructed architecture and handover. Android source, virtual-machine capability, storage allocation, and a distributable APK must be treated as unverified until built and tested on the intended device.

## Engineering principles

- Use supported Android APIs and explicit permissions.
- Avoid root access, firmware flashing, and destructive partition changes.
- Keep secrets out of client applications.
- Distinguish implemented features from specifications and simulations.
- Build reproducibly and test on the target Android version.