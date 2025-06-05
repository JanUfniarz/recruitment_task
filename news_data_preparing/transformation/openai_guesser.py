import os

with open("../data/api_key.txt", "r") as f:
    api_key = f.read().strip()

from openai import OpenAI
os.environ["OPENAI_API_KEY"] = api_key


class OpenAiGuesser:
    def __init__(self, city_state_map: dict[str, list[str]]):
        self.city_state_map = city_state_map
        self.states = list(city_state_map.keys())
        self.client = OpenAI()


    def __call__(self, state: str | None, city: str | None, url: str) -> tuple[str, str]:

        if state and city:
            return state, city

        new_state = state

        def guess(guess_state: bool = False) -> str:
            singular, plural = ('state', 'states') if guess_state else ('city', 'cities')
            options = ', '.join(self.city_state_map[new_state] if not guess_state else self.states)

            system_prompt = (
                f"You are a helpful assistant. Your task is to determine the US {singular} from the given patch.com URL. "
                f"Reply only with the full name of the {singular}. "
                f"Here is the list of available {plural}:\n{options}\n"
                "Dont reply Anything Other than exactly what's on the list"
                "for example, you can't say New York City because on list there is New York"
                f"If unsure, choose the closest matching {singular}."
            )

            user_prompt = f"Based on this URL, determine the {singular}: {url}"

            try:
                response = self.client.chat.completions.create(
                    model="gpt-4o-mini",
                    messages=[
                        {"role": "system", "content": system_prompt},
                        {"role": "user", "content": user_prompt},
                    ],
                    temperature=0.2,
                )

                result = response.choices[0].message.content.strip()

                if result in options:
                    print(f'info: model zwrócił {singular}: {result}')
                    return result
                else:
                    print(f"Ostrzeżenie: model zwrócił '{result}', którego nie ma na liście {plural}.")
                    return "Unknown"
            except Exception as e:
                print("Error during OpenAI call:", e)
                return "Unknown"

        if not new_state:
            new_state = guess(guess_state=True)

        if new_state not in self.city_state_map:
            return 'Unknown', 'Unknown'

        return new_state, guess()
