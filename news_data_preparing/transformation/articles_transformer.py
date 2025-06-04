import json
import os
from urllib.parse import urlparse

from openai_guesser import OpenAiGuesser

def normalize(text):
    return text.strip().replace("-", " ").lower()

counter: int = 0

def resolve_city_state(article: dict[str, str | int], guesser: OpenAiGuesser) -> dict[str, str]:
    global counter
    counter += 1
    print(f"article:{counter}")
    if article['type'] == 'across-america':
        return dict(
            id = counter,
            url=article["url"],
            title=article["title"],
            date=article["date"],
            text=article["text"],
            city='GLOBAL',
            state='GLOBAL'
        )

    path_parts = urlparse(article["url"]).path.strip("/").split("/")
    state_slug = path_parts[0] if len(path_parts) > 0 else ""
    city_slug = path_parts[1] if len(path_parts) > 1 else ""

    matched_state: str | None = None
    matched_city: str | None = None

    for state in city_state_map:
        if normalize(state) == normalize(state_slug):
            matched_state = state
            break

    if matched_state:
        for city in city_state_map[matched_state]:
            if normalize(city) == normalize(city_slug):
                matched_city = city
                break


    matched_state, matched_city = guesser(matched_state, matched_city, article['url'])

    return dict(
        id = counter,
        url=article["url"],
        title=article["title"],
        date=article["date"],
        text=article["text"],
        city=matched_city,
        state=matched_state
    )


if __name__ == '__main__':
    with open("../data/patch_articles_20250604_144742.json", "r", encoding="utf-8") as f:
        articles = json.load(f)['articles']

    with open("../../src/main/resources/data/us_states_and_cities.json", "r", encoding="utf-8") as f:
        city_state_map = json.load(f)

    openai_guesser = OpenAiGuesser(city_state_map)
    transformed = [resolve_city_state(article, openai_guesser) for article in articles]

    with open("../data/transformed_articles_v2.json", "w", encoding="utf-8") as f:
        json.dump(transformed, f, ensure_ascii=False, indent=2)
